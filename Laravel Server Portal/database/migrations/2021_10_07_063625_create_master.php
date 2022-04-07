<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateMaster extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('masters', function (Blueprint $table) {
            $table->unsignedBigInteger('userId')->nullable()->constraint('users');
            $table->unsignedBigInteger('petId')->nullable()->constraint('pets');
            $table->unsignedBigInteger('hygieneId')->nullable()->constraint('hygiene');
            $table->unsignedBigInteger('recordId')->nullable()->constraint('records');
            $table->unsignedBigInteger('cleanId')->nullable()->constraint('cleaning');
            $table->unsignedBigInteger('saleId')->nullable()->constraint('sales');
            $table->unsignedBigInteger('paymentId')->nullable()->constraint('payments');
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
        Schema::dropIfExists('masters');
    }
}
