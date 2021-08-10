<?php

namespace App\Http\Controllers;

use App\Http\Resources\WardResource;
use App\Models\Ward;
use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class WardController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Resources\Json\AnonymousResourceCollection
     */
    public function index()
    {
        return WardResource::collection(Ward::all());
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
            'name' => 'required|unique:wards',
        ]);

        $ward = Ward::create($data);

        if($ward) {
            $ward->status = 1;
            $ward->save();
            return response()->json([
                'ward' => new WardResource($ward),
                'message' => 'Ward is successfully created.'
            ]);
        }

        return response()->json([
            'message' => 'Ward is not stored.'
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
        $ward = Ward::find($id);

        if ($ward) {
            return response()->json([
                'ward' => new WardResource($ward)]
            );
        }

        return response()->json([
            'message' => 'Ward is not found.'
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
            'name' => 'required|unique:wards,name,' . $id,
        ]);

        $ward = Ward::find($id);

        if($ward) {
            $ward->update([
                'name' => $request->name ? $request->name : $ward->name,
            ]);

            return response()->json([
                'ward' => new WardResource($ward),
                'message' => 'Ward is successfully updated.'
            ]);
        }

        return response()->json([
            'message' => 'Ward is not updated.'
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
        $ward = Ward::find($id);
        if ($ward) {
            $ward->delete();
            return response()->json([
                'message' => 'Ward is deleted successfully.'
            ]);
        }

        return response()->json([
            'message' => 'This Ward is not deleted.'
        ]);
    }
}
