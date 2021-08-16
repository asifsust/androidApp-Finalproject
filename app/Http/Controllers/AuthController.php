<?php

namespace App\Http\Controllers;

use App\Http\Resources\EmployeeResource;
use App\Http\Resources\UserResource;
use App\Mail\SendEmailVerificationCode;
use App\Mail\SendMail;
use App\Models\Employee;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Facades\Storage;
use Illuminate\Validation\Rule;

class AuthController extends Controller
{
    private function isUserIdExists($userId) {
        $users = User::where('user_id', $userId)->get();

        // value present in db
        if ($users->isNotEmpty()) {
            return true;
        }

        // not present in db
        return false;
    }

    /**
     * @throws \Exception
     */
    private function generateUserId() {

        $userId = random_int(100000,999999);

        if ($this->isUserIdExists($userId)) {
            $this->generateUserId();
        }

        return $userId;
    }

    /**
     * @throws \Exception
     */
    public function register(Request $request)
    {
        $request->validate([
            'name'          =>  'required|string',
            'mobile'        =>  'required',
            'email'         =>  'required|string|unique:users,email',
            'password'      =>  'required|string|confirmed|min:6',
            'image'         =>  'mimes:jpg,bmp,png|nullable',
            'date_of_birth' =>  'required',
            'joining_date'  =>  'required',
        ]);

        // if request has user_id and exist in database
        if ($request->filled('user_id') && $this->isUserIdExists($request->user_id)) {
            return response()->json([
                'message' => 'User id has already been taken.'
            ]);
        }

        $user_id = $request->user_id;

        // auto generate userId if not present in request userid
        if (! $request->filled('user_id')) {
            $user_id = $this->generateUserId();
        }

        // register an user
        $user = User::create([
            'name'          => $request->name,
            'user_id'       => $user_id,
            'mobile'        => $request->mobile,
            'email'         => $request->email,
            'password'      => Hash::make($request->password),
            'role_id'       => $request->role_id ?? 2,
        ]);

        // after that create an employee with user
        $employee = Employee::create([
            'user_id'           => $user->id,
            'date_of_birth'     => $request->date_of_birth,
            'joining_date'      => $request->joining_date,
            'created_by'        => auth()->user() ? auth()->id() : $user->id,
        ]);

        if($request->hasFile('image')){
            $employee->image = $request->image->store('employees', 'public');
            $employee->update();
        }

        // create a token for an authentication
        $token = $user->createToken('myapptoken')->plainTextToken;

        Mail::to($user->email)->send(
            new SendMail(['email' => $request->email,'password' => $request->password ])
        );

        $response = [
            'user' => $user,
            'employee' => $employee,
            'token'     => $token
        ];

        return response($response, 201);
    }

    public function update(Request $request, $userId, $employeeId)
    {
        $user = User::findOrFail($userId);
        $employee = Employee::findOrFail($employeeId);

        if (! $user || ! $employee) {
            return response()->json([
                'message' => 'Invalid User!'
            ]);
        }

        $request->validate([
            'name'          =>  'required|string',
            'mobile'        =>  'required',
            'email'         =>  'required|string|unique:users,email,' . $user->id,
            'password'      =>  'nullable|string|min:6',
            'image'         =>  'mimes:jpg,bmp,png|nullable',
            'date_of_birth' =>  'required',
            'joining_date'  =>  'required',
        ]);

        $user_id = !$request->filled('user_id') ? $request->user_id : $user->user_id;


        // register an user
        $user->update([
            'name'          => $request->name ?? $user->name,
            'mobile'        => $request->mobile ?? $user->mobile,
            'email'         => $request->email ?? $user->email,
            'role_id'       => $request->role_id ?? $user->role_id,
        ]);

        if ($request->filled('password')) {
            $user->update([
                'password'           => Hash::make($request->password),
                'is_update_password' => 1,
            ]);
        }

        // after that create an employee with user
        $employee->update([
            'user_id'           => $user->id,
            'date_of_birth'     => $request->date_of_birth ?? $employee->date_of_birth,
            'joining_date'      => $request->joining_date ?? $employee->joining_date,
            'created_by'        => auth()->user() ? auth()->id() : $user->id,
            'updated_by'        => auth()->user() ? auth()->id() : $user->id,
        ]);

        // if request has image
        if($request->hasFile('image')){
            Storage::disk('public')->delete($employee->image);
            $employee->image = $request->image->store('employees', 'public');
            $employee->update();
        }

        $response = [
            'user' => $user,
            'employee' => $employee,
        ];

        return response($response, 201);
    }

    public function logout(Request $request) {
        auth()->user()->tokens()->delete();

        return [
            'message' => 'Logged Out'
        ];
    }

    public function login(Request $request)
    {
        $request->validate([
            'email'=> 'required|email|string',
            'password' => 'required|string'
        ]);

        // check email
        $user = User::where('email', $request->email)->first();

        //check user is exist or not
        if (!$user || $user->status === 0) {
            return response()->json([
                'message' => "The User is deleted"
            ]);
        }

        // check password
        if(!$user || !Hash::check($request->password, $user->password)) {
            return response([
                'message' => 'Bad credentials'
            ], 401);
        }

        // create bearer token for authentication
        $token = $user->createToken('myapptoken')->plainTextToken;

        $response = [
            'user' => new UserResource($user),
            'employee' => $user->employee,
            'token'=> $token
        ];

        return response($response, 201);
    }

    public function changePassword(Request $request)
    {
        // validate request
        $fields = $request->validate([
            'current_password'      => 'required|string',
            'new_password'          => 'required|string',
            'password_confirmation'  => 'required|string|same:new_password'
        ]);

        // find a user
        $user = auth()->user();
        $user->password = Hash::make($fields['new_password']);
        $user->is_update_password = 1;

        // save user
        $user->save();

        $response = [
            'success' => 'Password changed successfully!!!',
        ];

        // return response
        return $response;
    }

    public function destroy(Request $request,$user_id)
    {
        $user = User::find($user_id);

        if($user) {
            // user role
            $role = ucfirst($user->role->name);

            $user->employee->update(['status' => 0]);
            $user->status = 0;
            $user->save();

            return response()->json([
                'message' => "$role is successfully deleted."
            ]);
        }

        return response()->json([
            'message' => "This user doesn't exist."
        ]);
    }






    // recover password
    public function sendEmailVerificationCode(Request $request)
    {
	    $user_count = User::where('email', $request->email)
		    ->where('status','!=',0)
		    ->count();

        if ($user_count < 1) {
            return response()->json([
                'message' => "Email Not Found!"
            ]);
        }

        try {

            Mail::to($request->email)->send(new SendEmailVerificationCode($verification_code = rand(100000, 999999)));



            return response()->json([
                'message' => "Successfully Send",
                'code' => $verification_code
            ]);
        } catch (\Exception $ex) {

            return response()->json([
                'message' => "Some Issues Occurred, Please Try later " . $ex->getMessage()
            ]);
        }
    }



    public function recoverUserPassword(Request $request)
    {
        // validate request
        $fields = $request->validate([
            'email'                 => 'required|email',
            'new_password'          => 'required|string',
            'password_confirmation' => 'required|string|same:new_password'
        ]);

        // find a user
	$user = User::where('email', $request->email)
		->where('status', '!=',0)
		->first();

        if (!$user) {
            $response = [
                'message' => 'User Not Found By This Email'
            ];

            // return response
            return response()->json($response);
        }


        $user->password = Hash::make($fields['new_password']);
        $user->is_update_password = 1;

        // save user
        $user->save();

        $response = [
            'message' => 'Password successfully Updated!!',
            'password' => $request->new_password
        ];

        // return response
        return response()->json($response);
    }
}
