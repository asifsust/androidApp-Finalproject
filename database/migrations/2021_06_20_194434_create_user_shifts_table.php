<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateUserShiftsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('user_shifts', function (Blueprint $table) {
            $table->id();
            $table->text('description')->nullable();
            $table->text('timeshift')->nullable();
            $table->foreignId('user_id')->constrained('users');
            $table->foreignId('ward_id')->constrained('wards');
            $table->foreignId('shift_id')->constrained('shifts');
            $table->date('date');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('employee_shifts');
    }
}
