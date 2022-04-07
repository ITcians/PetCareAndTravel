<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePets extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('pets', function (Blueprint $table) {
            $table->id('petId');
            $table->string('petName');
            $table->string('petPhoto')->nullable();
            $table->string('petAge')->default("No Age");
            $table->string('petSpeice');
            $table->string('petGender');
            $table->string('petAddress');
            $table->double('petPrice')->default(0);
            $table->string("petHeight");
            $table->string('petWeight');
            $table->double('petLat');
            $table->double('petLang');
            $table->string('petContactNo');
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
        Schema::dropIfExists('pets');
    }
}
