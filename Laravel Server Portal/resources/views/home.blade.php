@extends('layouts.app')
@section('title', 'Dashbord')
@section('content')
<div class="container">
    <div class="row justify-content-center">
        <div class='col-lg-3 col-xs-12'>
            <div class='card'>
                <div class='card-body'>
                    <a class='nav-link'>
                        <span style='color:var(--orange);font-size:30px;font-weight:bold'>
                            <i class='fa fa-paw'></i>
                            {{ $pets }}
                        </span>
                        <br>
                        <span style='color:var(--orange);'>Pets</span>

                    </a>
                </div>
            </div>
        </div>
        <div class='col-lg-3 col-xs-12'>
            <div class='card'>
                <div class='card-body'>
                    <a class='nav-link' href="/users">
                        <span style='color:var(--blue);font-size:30px;font-weight:bold'>
                            <i class='fa fa-users'></i>
                            {{ $users }}
                        </span>
                        <br>
                        <span style='color:var(--blue);'>Users</span>

                    </a>
                </div>
            </div>
        </div>
        <div class='col-lg-3 col-xs-12'>
            <div class='card'>
                <div class='card-body'>
                    <a class='nav-link' href="/viewSales">
                        <span style='color:var(--green);font-size:30px;font-weight:bold'>
                            <i class='fa fa-money'></i>
                            {{ $sales }}
                        </span>
                        <br>
                        <span style='color:var(--green);'>Sales</span>

                    </a>
                </div>
            </div>
        </div>
        <div class='col-lg-3 col-xs-12'>
            <div class='card'>
                <div class='card-body'>
                    <a class='nav-link' href="/orders">
                        <span style='color:var(--pink);font-size:30px;font-weight:bold'>
                            <i class='fa fa-shopping-cart'></i>
                            {{ $orders }}
                        </span>
                        <br>
                        <span style='color:var(--pink);'>New Orders</span>

                    </a>
                </div>
            </div>
        </div>
        <div class='col-lg-3 col-xs-12' id="viewdoctor">
            <br>
            <div class='card'>
                <div class='card-body'>
                    <a class='nav-link' href="/doctors">
                        <span style='color:var(--red);font-size:30px;font-weight:bold'>
                            <i class='fa fa-hospital-o'></i>
                            {{ $doctors }}
                        </span>
                        <span style='color:var(--red);'>Doctors</span>

                    </a>
                </div>
            </div>
        </div>
        <div class='col-lg-12 col-xs-12'>
            <hr>
            <div class='card'>
                <div class='card-header'>
                    <h3>Activities/Notifications</h3>
                </div>
                <div class='card-body'>
                    <table class='table striped'>
                        <thead>
                            <tr>
                                <td>#</td>
                                <td>Activity</td>
                                <td>Send To</td>
                                <td>Made On</td>
                            </tr>
                        </thead>
                        <tbody>
                            @if(sizeof($notifications)>0)
                            @foreach($notifications as $not)
                            <tr>
                                <td>
                                    {{$loop->index+1}}
                                </td>
                                <td>
                                    {{$not->notification}}
                                </td>
                                <td>{{$not->email}} ({{$not->name}})</td>
                                <td>{{$not->created_at}}</td>
                            </tr>
                            @endforeach
                            @else
                            <tr>
                                <td>
                                    <p class='alert alert-warning'>No activity yet!</p>
                                </td>
                            </tr>
                            @endif
                        </tbody>
                    </table>
                </div>
                <div class='card-footer'>
                    {{$notifications->links('pagination::bootstrap-4')}}
                </div>

            </div>
        </div>
    </div>
</div>


<script>
    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
        }
    });

    $("#viewdoctor").click(function(event) {
        event.preventDefault();
        alert();
        // let name = $("input[name=name]").val();

        // $.ajax({
        //   url: "/ajax-request",
        //   type:"POST",
        //   data:{
        //     name:name,
        //     email:email,
        //     mobile_number:mobile_number,
        //     message:message,
        //     _token: _token
        //   },
        //   success:function(response){
        //     console.log(response);
        //     if(response) {
        //       $('.success').text(response.success);
        //       $("#ajaxform")[0].reset();
        //     }
        //   },
        //   error: function(error) {
        //    console.log(error);
        //   }
        //  });
    });
</script>
@endsection