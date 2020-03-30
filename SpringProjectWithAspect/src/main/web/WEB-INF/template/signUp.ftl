<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SignUp</title>
</head>
<body>
    <h1>
        Registration
    </h1>
    <hr>
    <form action="/signUp" method="post" >
        <p>
            <label>Name <input type="text" name="name" placeholder="Name"></label>
        </p>
        <p>
            <label>Mail <input type="text" name="mail" placeholder="Mail"></label>
        </p>
        <p>
            <label>Password <input type="password" name="password" placeholder="Password"></label>
        </p>
        <p>
            <input type="submit" value="Create account">
        </p>
    </form>
</body>
</html>