<?php

namespace Database\Seeders;

use App\Models\Role;
use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

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
        User::firstOrCreate(
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
    }
}
