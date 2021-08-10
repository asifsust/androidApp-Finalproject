<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UserShift extends Model
{
    use HasFactory;

    protected $guarded =[];

    public function user() {
        return $this->belongsTo(User::class);
    }
    public function shift() {
        return $this->belongsTo(Shift::class);
    }
    public function ward() {
        return $this->belongsTo(Ward::class);
    }
}
