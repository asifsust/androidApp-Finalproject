<?php

use App\Http\Controllers\ShiftController;
use App\Http\Controllers\UserShiftController;
use App\Http\Controllers\WardController;
use App\Http\Resources\UserResource;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\EmployeeController;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

//public routes
Route::post('register', [AuthController::class,'register']);
Route::post('login', [AuthController::class, 'login']);

// protected routes
Route::group(['middleware' => ['auth:sanctum']], function() {

    // get user info
    Route::get('user', function (Request $request) {
        return new UserResource($request->user());
    });

    // logout from device
    Route::post('logout', [AuthController::class,'logout']);

    // change image
    Route::post('change-image',[EmployeeController::class,'changeEmployeeImage'])->name('users.employee.change-image');

    // update user and employee
    Route::post('user/{user_id}/employee/{employee_id}',[AuthController::class,'update'])->name('users.employee.update');

    // destroy user and employee
    Route::post('user/{user_id}',[AuthController::class,'destroy'])->name('users.employee.destroy');

    // change user password
    Route::post('change-password',[AuthController::class,'changePassword'])->name('users.employee.change-password');

    // crud shifts
    Route::resource('shifts', ShiftController::class)->only(['index', 'store', 'show', 'destroy']);
    Route::post('shifts/{shift}',[ShiftController::class, 'update'])->name('shifts.update');

    // crud wards
    Route::resource('wards', WardController::class)->only(['index', 'store', 'show', 'destroy']);
    Route::post('wards/{ward}',[WardController::class, 'update'])->name('wards.update');

    // employees route
    Route::post('employees', [EmployeeController::class, 'index'])->name('employees.index');

    // user shift assign
    Route::post('user-shift-create',[UserShiftController::class, 'userShiftCreate'])->name('user-shift-create');
    Route::post('get-shift-assign-infos',[UserShiftController::class, 'getShiftAssignInfos'])->name('get-shift-assign-infos');
    Route::post('get-today-assign-shift',[UserShiftController::class, 'getTodayAssignShift'])->name('get-today-assign-shift');
    Route::post('get-all-assign-shift',[UserShiftController::class, 'getAllAssignShift'])->name('get-all-assign-shift');
    Route::post('edit-user-shift/{user_shift_id}',[UserShiftController::class, 'userShiftEdit'])->name('edit-user-shift');
    Route::post('delete-user-shift/{user_shift_id}',[UserShiftController::class, 'deleteUserShift'])->name('delete-user-shift');
    Route::post('get-timesheet-data',[UserShiftController::class, 'getTimeSheetData'])->name('get-timesheet-data');
    Route::post('view-employee-roaster',[UserShiftController::class, 'viewEmployeeRoaster'])->name('viewEmployeeRoaster');
});
