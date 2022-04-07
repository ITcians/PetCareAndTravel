<?php

use App\Http\Controllers\DoctorController;
use App\Http\Controllers\DryControl;
use App\Http\Controllers\PetController;
use App\Http\Controllers\UserController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

Route::post("/signup", [UserController::class, 'appSignUp']);
Route::post('/login', [UserController::class, 'appLogin']);
Route::post("/updateUser", [UserController::class, 'update']);

Route::get('/getHygieneReport', [PetController::class, 'getHygieneReports']);
Route::get('/searchDoctor', [DoctorController::class, 'search']);


Route::middleware('ensuretoken')->group(function () {
    Route::post('/updateFCM', [UserController::class, 'updateFCM']);
    Route::post('/addPet', [PetController::class, 'addPet']);
    Route::post('/updatePet', [PetController::class, 'update']);
    Route::post('/deletePet', [PetController::class, 'delete']);
    Route::post('/placeOrder', [PetController::class, 'addOrder']);
    Route::post('/addHygiene', [PetController::class, 'addHygiene']);
    Route::get('/getNotifications', [UserController::class, 'getUserNotifications']);
    Route::post('/readNotification', [UserController::class, "readNotification"]);
    Route::post('/addRecord', [PetController::class, 'addRecord']);
    Route::post('/addcleaning', [PetController::class, 'addCleaning']);
    Route::post('/addnews', [PetController::class, 'addnews']);
    Route::post('/payment', [PetController::class, 'payment']);
    Route::post('/sales', [PetController::class, 'sales']);
    Route::get('/getPets', [PetController::class, 'getPets']);
    Route::get('/getPet', [PetController::class, 'getPet']);
    Route::get('/getOrders', [PetController::class, 'getUserOrders']);
});
