@extends('layouts.app')
@section('title','Doctors')
@section('content')
<div class='col-lg-12 col-xs-12'>
    <div class='card'>
        <div class='card-header'>
            <h3>Add Doctors</h3>
        </div>
        <div class='card-body'>
            <div class="container">
                <div class="row">
                  <div class="col-sm-3"></div>
                  <div class="col-sm-6">
                    <form method="POST" action="/addDoctorProcess">
                      @csrf
                        <div class="form-group">
                          <label for="exampleInputEmail1">Name</label>
                          <input type="text" name="doctorname" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter Name">
                        </div>
                        <div class="form-group">
                            <label for="exampleFormControlTextarea1">Address</label>
                            <textarea class="form-control" name="doctoraddress" id="exampleFormControlTextarea1" rows="1"></textarea>
                          </div>
                          <div class="form-group">
                            <label for="exampleInputEmail1">Specification</label>
                            <input type="text" name="specification" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter Specification">
                          </div>
                          <div class="form-group">
                            <label for="exampleInputEmail1">Contact</label>
                            <input type="number" name="contact" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter Contact Number">
                           </div>
                           <input type="hidden" name="_token" value="{{ csrf_token() }}" />
                        <button type="submit" class="btn btn-primary">Add Doctor</button>
                      </form>
                  </div>
                  <div class="col-sm-3"></div>
                </div>
              </div>
        </div>
    </div>
</div>
@endsection