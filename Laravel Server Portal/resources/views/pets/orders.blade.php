@extends('layouts.app')
@section('title','Sales')
@section('content')
<div class='col-lg-12 col-xs-12'>
    <div class='card'>
        <div class='card-header'>
            <h3>Orders</h3>
        </div>
        <div class='card-body'>

            <table class='table striped'>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Pet Name</th>
                        <th>Pet Price</th>
                        <th>User</th>
                        <th>Payment</th>
                        <th>Payment Method</th>
                        <th>Payment Status</th>
                        <th>PickUp</th>
                        <th>DropOf</th>
                        <th>Placed at</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    @if(sizeof($orders)>0)
                    @foreach($orders as $o)
                    <tr>
                        <td>{{$loop->index+1}}</td>
                        <td>{{$o->petName}}</td>
                        <td>{{$o->petPrice}}</td>
                        <td>{{$o->accountType}}</td>
                        <td>{{$o->payment}}</td>
                        <td>{{$o->paymentMethod}}</td>
                        <td>{{$o->paymentStatus}}</td>
                        <td><a class='nav-link' href="https://maps.google.com/?q={{$o->pickUpLat}},{{$o->pickUpLang }}">
                                <i class='fa fa-map-marker'></i>
                            </a></td>
                        <td><a class='nav-link' href="https://maps.google.com/?q={{$o->dropOfLat}},{{$o->dropOfLang }}">
                                <i class='fa fa-map-marker'></i>
                            </a></td>
                        <td>{{$o->created_at}}</td>
                        <td>
                            <div class='row'>
                                <form action="/order-accept" method="POST" style='margin-right:7px'>
                                    @csrf
                                    <input type='hidden' value="{{$o->saleId}}" name="orderId">
                                    <input type='hidden' value="{{$o->paymentId}}" name="paymentId">
                                    <button type='submit' class="btn btn-success"><i class='fa fa-check-circle'></i></button>
                                </form>
                                <form action="/order-delete" method="POST">
                                    @csrf
                                    <input type='hidden' value="{{$o->saleId}}" name="orderId">
                                    <input type='hidden' value="{{$o->paymentId}}" name="paymentId">
                                    <button type='submit' class="btn btn-danger"><i class='fa fa-ban'></i></button>
                                </form>
                            </div>
                        </td>
                    </tr>
                    @endforeach
                    @else
                    <tr>
                        <p class='alert alert-warning'>No Sales found!</p>
                    </tr>
                    @endif
                </tbody>
            </table>
        </div>
        <div class='card-footer'>
            {{$orders->links('pagination::bootstrap-4')}}
        </div>
    </div>
</div>
@endsection