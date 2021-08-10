<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class UserShiftResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        return [
            'id'    => $this->id,
            'description'   => $this->description,
            'user'  => new UserResource($this->user),
            'ward'  => new WardResource($this->ward),
            'shift' => new ShiftResource($this->shift),
            'date'  => $this->date
        ];
    }
}
