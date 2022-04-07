@extends('layouts.app')
@section('title','Doctors')
@section('content')
<div class='col-lg-12 col-xs-12'>
    <div class='card'>
        <div class='card-header'>
            <h3>All Doctors</h3>
            @if(session('msg'))
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>{{ session('msg') }}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            @endif
            <strong class="success-msg"></strong>
        </div>
        <div class='card-body'>

            <table class='table striped'>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Address</th>
                        <th>Specification</th>
                        <th>Contact</th>
                        <th>Created At</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
              
                    @if(sizeof($doctors)>0)
                    @foreach($doctors as $doctor)
                    <tr>
                        <td>{{$loop->index+1}}</td>
                        <td>{{$doctor->doctorName}}</td>
                        <td>{{$doctor->doctorAddress}}</td>
                        <td>{{$doctor->specification}}</td>
                        <td>{{$doctor->contact}}</td>
                        <td>{{$doctor->created_at}}</td>
                        <td>
                            <a href="{{ url('editDoctor', ['id' => $doctor->doctorId])}}" class="btn btn-success">Edit</a>
                            <a href="{{ url('deleteDoctor', ['id' => $doctor->doctorId])}}" class="btn btn-danger">Delete</a>
                        </td>
                    </tr>
                    @endforeach
                    @else
                    <tr>
                        <p class='alert alert-warning'>No Doctors found!</p>
                    </tr>
                    @endif
                </tbody>
            </table>
        </div>
    </div>
</div>
@endsection