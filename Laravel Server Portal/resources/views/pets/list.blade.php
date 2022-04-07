@extends('layouts.app') @section('title','Pets') @section('content')
<div class='col-lg-12 col-xs-12'>
    <div class='card'>
        <div class='card-header'>
            <h3>Pets</h3>
        </div>
        <div class='card-body'>

            <table class='table striped'>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Image</th>
                        <th>Pet</th>
                        <th>Address</th>
                        <th>Spiece</th>
                        <th>Height</th>
                        <th>Weight</th>
                        <th>Age</th>
                        <th>Gender</th>
                    </tr>
                </thead>
                <tbody>

                    @if(sizeof($pets)>0) @foreach($pets as $pet)
                    <tr>
                        <td>{{$loop->index+1}} </td>
                        <td>
                            <img src="images/{{$pet->petPhoto}}" width="50" height="50" style="border-radius:50%">

                        </td>
                        <td>{{$pet->petName}}</td>
                        <td><a target="_blanck" href="#">{{$pet->petAddress}}</a></td>
                        <td>{{$pet->petSpeice}}</td>
                        <td>{{$pet->petHeight}}</td>
                        <td>{{$pet->petWeight}}</td>
                        <td>{{$pet->petGender}}</td>
                        <td>{{$pet->petAge}}</td>
                    </tr>
                    @endforeach @else
                    <tr>
                        <p class='alert alert-warning'>No pets found!</p>
                    </tr>
                    @endif
                </tbody>
            </table>
        </div>
        <div class='card-footer'>
            {{$pets->links('pagination::bootstrap-4')}}
        </div>
    </div>
</div>
@endsection