<?php

namespace App\Http\Controllers;

use App\Helpers\PaginationHelper;
use App\Http\Resources\ShiftResource;
use App\Http\Resources\UserResource;
use App\Http\Resources\UserShiftResource;
use App\Http\Resources\WardResource;
use App\Models\Shift;
use App\Models\User;
use App\Models\UserShift;
use App\Models\Ward;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class UserShiftController extends Controller
{
    /**
     * create user shift
     *
     * @param Request $request
     */
    public function userShiftCreate(Request $request)
    {
        $fields = $request->validate([
            'user_id' => "required",
            'shift_id' => 'required',
            'ward_id' => 'required',
            'date' => 'required'
        ]);

        $timeshift = Carbon::parse($request->date)->addDays(1)->format('Y-m-d') . now()->format(' H:i:s');

        $userShiftAssign = UserShift::create([
            'user_id' => $fields['user_id'],
            'shift_id' => $fields['shift_id'],
            'timeshift' => $timeshift,
            'description' => $request->description ? $request->description : null,
            'ward_id' => $fields['ward_id'],
            'date' => $fields['date'],
        ]);

        if ($userShiftAssign) {
            return response()->json([
                'user' => new UserResource($userShiftAssign->user),
                'shift' => new ShiftResource($userShiftAssign->shift),
                'description' => $userShiftAssign->description,
                'ward' => new WardResource($userShiftAssign->ward),
                'date' => $userShiftAssign->date,
                'message' => 'User Shift Assign is successfully created.'
            ]);
        }

        return response()->json([
            'message' => 'User shift assign is not saved.'
        ]);
    }

    public function getShiftAssignInfos()
    {
        return response()->json([
            'shifts' => ShiftResource::collection(Shift::all()),
            'wards' => WardResource::collection(Ward::all()),
        ]);
    }


    public function getTodayAssignShift()
    {
        $todayAssignShift = UserShift::where('date', '=', date('Y-m-d'))
            ->get();
        if ($todayAssignShift->isNotEmpty()) {
            return response()->json([
                'today_assign_shifts' => UserShiftResource::collection($todayAssignShift),
                'message' => 'Success'
            ]);
        }
        return response()->json([
            'message' => 'Empty',
            'today_assign_shifts' => []
        ]);
    }

    public function getAllAssignShift(Request $request)
    {
        $allAssignShift = UserShiftResource::collection(UserShift::orderBy('date', 'DESC')->get());


        if ($request->filled('date')) {
            $allAssignShift = UserShiftResource::collection(UserShift::with('user', 'shift', 'ward')->where('date', '=', date('Y-m-d', strtotime($request->date)))
                ->orderBy('date', 'DESC')->paginate(10));
        }

        $assignShifts = [];
        $counter = 1;
        foreach ($allAssignShift->groupBy('date') as $key => $assignShift) {
            $assignShifts[] = [
                'id' => $counter++,
                'date' => $key,
                'assign_shift_info' => $assignShift
            ];
        }

        $showPerPage = 10;

        return PaginationHelper::paginate(collect($assignShifts), $showPerPage);

    }

    public function userShiftEdit(Request $request, $id)
    {

        $fields = $request->validate([
            'user_id' => "required",
            'shift_id' => 'required',
            'ward_id' => 'required',
            'date' => 'required'
        ]);

        $userShift = UserShift::find($id);

        if ($userShift) {

            $userShift->update([
                'user_id' => $fields['user_id'],
                'shift_id' => $fields['shift_id'],
                'description' => $request->description ? $request->description : $userShift->description,
                'ward_id' => $fields['ward_id'],
                'date' => $fields['date'],
            ]);

            return response()->json([
                'user' => new UserResource($userShift->user),
                'shift' => new ShiftResource($userShift->shift),
                'description' => $userShift->description,
                'ward' => new WardResource($userShift->ward),
                'date' => $userShift->date,
                'message' => 'User Shift Assign is successfully updated.'
            ]);

        }

        return response()->json([
            'message' => 'User shift assign is not saved.'
        ]);
    }

    public function deleteUserShift($id)
    {

        $userShift = UserShift::find($id);

        $timeshift = now()->format('Y-m-d H:i:s');

        $message = "User shift is not found!";
        if ($userShift) {
            $userTimeShift = UserShift::where('timeshift', '<', $timeshift)->where('id', $id)->count();

            if ($userTimeShift > 0) {
                $message = "Shift is already sent to timesheet";
            } else {
                $userShift->delete();
                $message = 'User shift is successfully deleted';
            }

        }

        return response()->json([
            'message' => $message
        ]);

    }

    public function getTimeSheetData(Request $request)
    {
        $carbon = Carbon::parse($request->date ?? now());
        $date = $carbon->format('Y-m-d');

        $from_date = $request->from_date ?? Carbon::parse($date)->subDays(5)->format('Y-m-d');
        $timeshift = $date . $carbon->format(' H:i:s');

        $timeSheets = UserShift::where('timeshift', '<=', $timeshift)
            ->where('date', ">=", $from_date)
            ->when($request->filled('user_id'), function ($q) use ($request) {
                $q->where('user_id', $request->user_id ?? auth()->id());
            })
            ->get();

        if ($timeSheets->isNotEmpty()) {
            return response()->json([
                'message' => "Success",
                'timesheets' => UserShiftResource::collection($timeSheets)
            ]);
        }

        return response()->json([
            'message' => 'Empty',
            'timesheets' => UserShiftResource::collection($timeSheets)
        ]);
    }

    public function viewEmployeeRoaster(Request $request)
    {
        $carbon = Carbon::parse($request->date ?? now());
        $date = $carbon->format('Y-m-d');

        $from_date = $request->from_date ?? Carbon::parse($date)->subDays(5)->format('Y-m-d');
        $to_date = $request->to_date ?? Carbon::parse($date)->addDays(5)->format('Y-m-d');

        $roasters = UserShift::where('date', '>=', $from_date)
            ->where('date', "<=", $to_date)
            ->get();

        if ($roasters->isNotEmpty()) {
            return response()->json([
                'message' => "Success",
                'roasters' => UserShiftResource::collection($roasters)
            ]);
        }

        return response()->json([
            'message' => 'Empty',
            'roasters' => UserShiftResource::collection($roasters)
        ]);
    }
}
