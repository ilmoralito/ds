<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="master"/>
    <title><g:layoutTitle/></title>
    <g:layoutHead/>
</head>
<body>
    <div class="span2">
        <g:render template="/layouts/sidebar"/>
    </div>
    <div class="span10">
        <g:pageProperty name="page.main"/>

        <g:render template="/layouts/errors"/>

        <ds:flashMessage>${flash.message}</ds:flashMessage>
    </div>
</body>
</html>
