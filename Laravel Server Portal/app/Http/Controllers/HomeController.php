<?php

namespace App\Http\Controllers;

use App\Models\Doctor;
use Illuminate\Http\Request;
use App\Models\Master;
use App\Models\Notifications;
use App\Models\Pets;
use App\Models\Sales;
use App\Models\User;
use Illuminate\Support\Facades\DB;

class HomeController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Contracts\Support\Renderable
     */
    public function index()
    {
    
        $noti = DB::table('notifications')->latest()
            ->join('users', 'users.id', 'notifications.notificationFor')
            ->select('name', 'email', 'notification', 'notifications.created_at')
            ->paginate(10);

        return view('home', [
            'orders' => Sales::where('saleStatus', 1)->count(), "pets" => Pets::count(),
            'doctors' => Doctor::count(), "users" => User::where('id', "!=", 1)->count(),
            "sales" => Sales::sum('saleFees'),
            "notifications" => $noti
        ]);
    }
}
