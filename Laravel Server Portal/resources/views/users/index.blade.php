@extends('layouts.app')
@section('title','Users')
@section('content')
<div class='col-lg-12 col-xs-12'>
    <div class='card'>
        <div class='card-header'>
            <h3>All Users</h3>
        </div>
        <div class='card-body'>

            <table class='table striped'>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Account Type</th>
                        <th>Email</th>
                        <th>Created At</th>
                    </tr>
                </thead>
                <tbody>
                    @if(sizeof($users)>0)
                    @foreach($users as $user)
                    <tr>
                        <td>{{$loop->index+1}}</td>
                        <td>{{$user->name}}</td>
                        <td>{{$user->accountType}}</td>
                        <td>{{$user->email}}}</td>
                        <td>{{$user->created_at}}</td>
                    </tr>
                    @endforeach
                    @else
                    <tr>
                        <p class='alert alert-warning'>No user found!</p>
                    </tr>
                    @endif
                </tbody>
            </table>
        </div>
    </div>
</div>
@endsection