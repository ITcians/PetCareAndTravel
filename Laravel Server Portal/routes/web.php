<?php

use App\Http\Controllers\Controller;
use App\Http\Controllers\UserController;
use App\Http\Controllers\PetController;
use App\Http\Controllers\DoctorController;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function(){
    return view('auth.login');
});

Auth::routes();

Route::get('/home', [App\Http\Controllers\HomeController::class, 'index'])->name('home');
Route::middleware('auth')->group(function(){
    // Route::get('/register',[Regis])
    Route::get('/users',[UserController::class,'index']);
    Route::get('/doctors',[DoctorController::class,'index']);
    Route::get('/addDoctor',[DoctorController::class,'addDoctor']);
    Route::post('/addDoctorProcess',[DoctorController::class,'addDoctorProcess']);
    Route::get('/deleteDoctor/{doctorId}',[DoctorController::class,'deleteDoctor']);
    Route::get('/editDoctor/{doctorId}',[DoctorController::class,'editDoctor']);
    Route::post('/updateDoctorProcess',[DoctorController::class,'updateDoctorProcess']);

    Route::get('/viewSales',[PetController::class,'viewsales']);
    Route::get('/orders',[PetController::class,'orders']);
    Route::post('/order-accept',[PetController::class,'orderComplete']);
    Route::post('/order-delete',[PetController::class,'orderDelete']);
    Route::get('/pets',[PetController::class,"petList"]);
    
});

Route::get('forget-password', [App\Http\Controllers\ForgotPasswordController::class, 'showForgetPasswordForm'])->name('forget.password.get');
Route::post('forget-password', [App\Http\Controllers\ForgotPasswordController::class, 'submitForgetPasswordForm'])->name('forget.password.post');
Route::get('reset-password/{token}', [App\Http\Controllers\ForgotPasswordController::class, 'showResetPasswordForm'])->name('reset.password.get');
Route::post('reset-password', [App\Http\Controllers\ForgotPasswordController::class, 'submitResetPasswordForm'])->name('reset.password.post');
