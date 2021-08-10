<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;
use Illuminate\Support\Facades\Storage;

class EmployeeResource extends JsonResource
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
            $this->merge(new UserResource($this->user)),
            'employee_id'               => $this->id,
            'image'                     => $this->image ? asset(Storage::url($this->image)) : $this->image,
            'date_of_birth'             => $this->date_of_birth,
            'joining_date'              => $this->joining_date,
        ];
    }
}
