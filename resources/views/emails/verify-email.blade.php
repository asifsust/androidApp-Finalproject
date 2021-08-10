@component('mail::message')
    ## Congratulations!! You account has been created. Please use below credentials to login.

    Email:  {{ $details['email'] }}
    Password:  {{ $details['password'] }}
@endcomponent
