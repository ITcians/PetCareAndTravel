@extends('layouts.app')
@section('title', 'Sales')
@section('content')
    <div class='col-lg-12 col-xs-12'>
        <div class='card'>
            <div class='card-header'>
                <h3>Sales</h3>
            </div>
            <div class='card-body'>

                <table class='table striped'>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>PickUp</th>
                            <th>DropOf</th>
                            <th>Sale Fee/Profits</th>
                            <th>Sale Status</th>
                            <th>Created At</th>
                        </tr>
                    </thead>
                    <tbody>

                        @if (sizeof($sales) > 0)
                            @foreach ($sales as $sale)
                                <tr>
                                    <td>{{ $loop->index + 1 }}</td>
                                    <td><a class='nav-link'
                                            href="https://maps.google.com/?q={{ $sale->pickUpLat }},{{ $sale->pickUpLang }}">
                                            <i class='fa fa-map-marker'></i>
                                        </a></td>
                                    <td><a class='nav-link'
                                            href="https://maps.google.com/?q={{ $sale->dropOfLat }},{{ $sale->dropOfLang }}">
                                            <i class='fa fa-map-marker'></i>
                                        </a></td>
                                    <td>{{ $sale->saleFees }}</td>
                                    <td>@if ($sale->saleStatus == 0) Completed @else Pending @endif</td>
                                    <td>{{ $sale->created_at }}</td>
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
                {{$sales->links('pagination::bootstrap-4')}}
            </div>
        </div>
    </div>
@endsection
