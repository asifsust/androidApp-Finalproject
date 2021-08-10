<?php

namespace Database\Seeders;

use App\Models\Employee;
use App\Models\Role;
use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\File;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Storage;

class UserTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $role = Role::where('name', 'manager')->first();
        $user = User::firstOrCreate(
            [
                'email' => 'admin@gmail.com',
                'user_id' => 999999,
            ],
            [
                'name' => "Admin",
                'mobile' => '01611223344',
                'role_id' => $role->id ?? 1,
                'password' => Hash::make('admin12345'),
            ]
        );

        Employee::firstOrCreate([
            'user_id'           => $user->id,
            'date_of_birth'     => "1994-01-01",
            'image'             => Storage::disk('public')->putFile("managers",__DIR__.'/manager.png','public'),
            'joining_date'      => "2020-01-01",
            'created_by'        => auth()->user() ? auth()->id() : $user->id,
        ]);
    }


}
