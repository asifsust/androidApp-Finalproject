@component('mail::message')
    # Hello!

    You have registered for {{ config('app.name') }}. Please verify your email address. You can click the button below to
    continue with verification.
    @component('mail::button', ['url' => route('deeplink.verify_email', ['code' => $verification_code])])
        Continue to verify
    @endcomponent

    Or, manually copy the below code into the app:
    @component('mail::panel')
        {{ $verification_code }}
    @endcomponent

    Thanks,<br>
    {{ config('app.name') }}
@endcomponent
