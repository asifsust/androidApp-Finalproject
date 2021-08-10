<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\App;
use League\Glide\Server;
use Illuminate\Support\Facades\URL;

class Employee extends Model
{
    use HasFactory;
    protected $guarded =[];

    protected $perPage = 20;
    public function user()
    {
        return $this->belongsTo(User::class);
    }

}
