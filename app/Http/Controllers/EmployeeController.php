<?php

namespace App\Http\Controllers;

use App\Http\Resources\EmployeeResource;
use App\Models\Employee;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class EmployeeController extends Controller
{
    public function changeEmployeeImage(Request $request)
    {
        $fields = $request->validate([
            'image' => 'nullable|image'
        ]);

        $user = auth()->user();
        $employee = Employee::where('user_id', $user->id)->first();

        if($request->hasFile('image')){
            Storage::disk('public')->delete($employee->image);
            $employee->image = $request->image->store('employees', 'public');
            $employee->update();
        }

        return response()->json([
            'success' => 'Image is successfully changed!!!'
        ]);
    }

    /**
     * @param Request $request
     */
    public function index(Request $request)
    {
        return EmployeeResource::collection(Employee::paginate());
    }
}
