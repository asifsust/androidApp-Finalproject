<?php

namespace Database\Seeders;

use App\Models\Role;
use Illuminate\Database\Seeder;

class RoleTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Role::updateOrCreate([
            'name' => 'manager',
        ]);

        Role::updateOrCreate([
            'name' => 'RGN',
        ]);

        Role::updateOrCreate([
            'name' => 'HCS',
        ]);

    }
}
