<?php

namespace App\Http\Resources;

use App\Models\Role;
use Illuminate\Http\Resources\Json\JsonResource;

class UserResource extends JsonResource
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
            'id'            =>  $this->id,
            'user_id'       => $this->user_id,
            'name'          =>  $this->name,
            'mobile'        => $this->mobile,
            'email'         => $this->email,
            'role'          => $this->role->name ?? 'employee'
        ];
    }
}
