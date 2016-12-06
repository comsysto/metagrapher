<!DOCTYPE html>
<html>
<head>
    <title>Metagrapher</title>

    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1">

    <link href="${applicationBasePath}/libs/bootstrap-3.3.4-dist/css/bootstrap.css"
          rel="stylesheet">
    <link href="${applicationBasePath}/libs/bootstrap-3.3.4-dist/css/bootstrap-theme.css"
          rel="stylesheet">
    <link href="${applicationBasePath}/libs/jquery.qtip.css" rel="stylesheet">
    <link href="${applicationBasePath}/css/metagrapher.css" rel="stylesheet">
    <link href="${applicationBasePath}/css/metagrapher-graph.css" id="graph-styles" rel="graph-styles">

    <script id="config" type="application/json">
        {
            "graphUrl":  "${applicationBasePath}/metagrapher/graph.json",
            "configUrl": "${applicationBasePath}/metagrapher/config"
        }
    </script>
</head>

<body>
<app>
Loading...
</app>
<script src="js/application.js"></script>
</body>
</html>