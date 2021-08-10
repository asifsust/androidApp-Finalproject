<?php

namespace App\Http\Controllers;

use App\Http\Resources\ShiftResource;
use App\Models\Shift;
use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class ShiftController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Resources\Json\AnonymousResourceCollection
     */
    public function index()
    {
        return ShiftResource::collection(Shift::all());
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $data = $request->validate([
            'name' => 'required|unique:shifts',
            'start_time' => 'required',
            'end_time'  => 'required'
        ]);

        $shift = Shift::create($data);

        if($shift) {
            $shift->status = 1;
            $shift->save();
            return response()->json([
                'shift' => new ShiftResource($shift),
                'message' => 'Shift is successfully created.'
            ]);
        }

        return response()->json([
            'message' => 'Shift is not stored.'
        ]);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $shift = Shift::find($id);

        if ($shift) {
            return response()->json([
                'shift' => new ShiftResource($shift)]
            );
        }

        return response()->json([
            'message' => 'Shift is not found.'
        ]);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $request->validate([
            'name' => 'required|unique:shifts,name,' . $id ,
            'start_time' => 'required',
            'end_time'  => 'required'
        ]);

        $shift = Shift::find($id);

        if($shift) {
            $shift->update([
                'name' => $request->name ? $request->name : $shift->name,
                'start_time' => $request->start_time ? $request->start_time : $shift->start_time,
                'end_time' => $request->end_time ? $request->end_time : $shift->end_time,
            ]);

            return response()->json([
                'shift' => new ShiftResource($shift),
                'message' => 'Shift is successfully updated.'
            ]);
        }

        return response()->json([
            'message' => 'Shift is not updated.'
        ]);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $shift = Shift::find($id);
        if ($shift) {
            $shift->delete();
            return response()->json([
                'message' => 'Shift is deleted successfully.'
            ]);
        }

        return response()->json([
            'message' => 'This Shift is not deleted.'
        ]);
    }
}
