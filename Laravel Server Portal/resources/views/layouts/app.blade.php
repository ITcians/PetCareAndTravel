<!doctype html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- CSRF Token -->
    <meta name="csrf-token" content="{{ csrf_token() }}">

    <title>@yield('title')</title>

    <!-- Scripts -->
    <script src="{{ asset('js/app.js') }}" defer></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <!-- Fonts -->
    <link rel="dns-prefetch" href="//fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css?family=Nunito" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <!-- Styles -->
    <link href="{{ asset('css/app.css') }}" rel="stylesheet">
</head>

<body>
    <div id="app">
        <nav class="navbar navbar-expand-md navbar-light bg-white shadow-sm">
            <div class="container">
                <a class="navbar-brand" href="{{ url('/home') }}">
                    <span style="color:var(--blue);font-size:20px;font-weight:bold"><i class='fa fa-paw'></i> Pet +Visor</span>
                </a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false"
                    aria-label="{{ __('Toggle navigation') }}">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <!-- Left Side Of Navbar -->
                    <ul class="navbar-nav mr-auto">

                    </ul>

                    <!-- Right Side Of Navbar -->
                    <ul class="navbar-nav ml-auto">
                        <!-- Authentication Links -->
                        @guest
                            @if (Route::has('login'))
                                <li class="nav-item">
                                    <a class="nav-link" href="{{ route('login') }}">{{ __('Login') }}</a>
                                </li>
                            @endif

                        @else

                            <li class="nav-item dropdown">
                                <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" v-pre>
                                    Pets
                                </a>

                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                                    <a class="dropdown-item" href="/pets">
                                        <span style='color:var(--blue)'><i class='fa fa-paw'></i> View Pet</span>
                                    </a>
                                </div>
                            </li>
                            <li class="nav-item dropdown">
                                <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" v-pre>
                                    Users
                                </a>

                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">


                                    <a class="dropdown-item" href="/users">
                                        <i class='fa fa-users'></i> View Users
                                    </a>
                                </div>
                            </li>

                            <li class="nav-item dropdown">
                                <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" v-pre>
                                    Sales
                                </a>

                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                                    <a class="dropdown-item" href="/viewSales">
                                        <i class='fa fa-line-chart'></i> View Sales
                                    </a>


                                </div>
                            </li>

                            <li class="nav-item dropdown">
                                <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" v-pre>
                                    Orders
                                </a>

                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                                    <a class="dropdown-item" href="/orders">
                                        <span style='color:var(--pink)'><i class='fa fa-clock-o'></i> Pending</span>
                                    </a>

                                    <a class="dropdown-item" href="/orders">
                                        <span style='color:var(--blue)'><i class='fa fa-check'></i> Approved</span>
                                    </a>

                                    <a class="dropdown-item" href="/orders">
                                        <span style='color:var(--green)'><i class='fa fa-check-circle'></i> Completed</span>
                                    </a>


                                </div>
                            </li>

                            <li class="nav-item dropdown">
                                <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" v-pre>
                                    Doctors
                                </a>

                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">

                                    <a class="dropdown-item" href="/addDoctor">
                                        <i class='fa fa-plus'></i> Add Doctors
                                    </a>
                                    <a class="dropdown-item" href="/doctors">
                                        <i class='fa fa-hospital-o'></i> View Doctors
                                    </a>


                                </div>
                            </li>

                            <li class="nav-item dropdown">
                                <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" v-pre>
                                    {{ Auth::user()->name }}
                                </a>

                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                                    {{-- <a class="dropdown-item" href="{{ route('logout') }}">
                                    <i class='fa fa-cog'></i> Settings
                                </a> --}}

                                    <a class="dropdown-item" href="{{ route('logout') }}" onclick="event.preventDefault();
                                                         document.getElementById('logout-form').submit();">
                                        <i class='fa fa-sign-out'></i> {{ __('Logout') }}
                                    </a>

                                    <form id="logout-form" action="{{ route('logout') }}" method="POST"
                                        class="d-none">
                                        @csrf
                                    </form>
                                </div>
                            </li>
                        @endguest
                    </ul>
                </div>
            </div>
        </nav>

        <main class="py-4">
            <div class='container'>
                @if (session('fail'))
                    <div class='card'>
                        <p class='alert alert-danger'>{{ session('fail') }}</p>
                    </div>
                @elseif(session('success'))
                    <div class='card'>
                        <p class='alert alert-success'>{{ session('success') }}</p>
                    </div>
                @elseif(session('info'))
                    <div class='card'>
                        <p class='alert alert-info'>{{ session('info') }}</p>
                    </div>
                @elseif(session('msg'))
                    <div class='card'>
                        <p class='alert alert-info'>{{ session('info') }}</p>
                    </div>
                @endif
                @yield('content')
            </div>

        </main>
    </div>
    <script src="{{ asset('js/jquery-3.4.1.min.js') }}"></script>
    <script src="{{ asset('js/popper.min.js') }}"></script>

    <script type="text/javascript" language="javascript">
        $(document).ready(function(event) {
            $.ajaxSetup({
                headers: {
                    "X-CSRF-TOKEN": $("meta[name='csrf-token']").attr("content")
                }
            });

            $(".deleteDoctor").click(function(event) {
                event.preventDefault();
                var doctorId = $(this).attr("doctorId");
                alert(doctorId);
                var request = $.ajax({
                    url: '/deleteDoctor',
                    method: 'POST',
                    data: {
                        doctorId: doctorId,
                        '_token': $('input[name=_token]').val()
                    },
                    success: function(data) {
                        alert(data);
                    }
                });

            });

            function printMsg(msg) {
                if ($.isEmptyObject(msg.error)) {
                    console.log(msg.success);
                    $('.alert-block').css('display', 'block').append('<strong>' + msg.success + '</strong>');
                    
                } else {
                    $.each(msg.error, function(key, value) {
                        $('.' + key + '_err').text(value);
                    });
                }
            }

        });
    </script>
</body>

</html>
