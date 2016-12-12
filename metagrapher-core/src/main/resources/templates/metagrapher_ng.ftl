<!DOCTYPE html>
<html>
<head>
    <title>Metagrapher</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1">
    <link href="${applicationBasePath}/libs/bootstrap-3.3.4-dist/css/bootstrap.css" rel="stylesheet">
    <link href="${applicationBasePath}/libs/bootstrap-3.3.4-dist/css/bootstrap-theme.css" rel="stylesheet">
    <link href="${applicationBasePath}/css/metagrapher.css" rel="stylesheet">
</head>

<body class="graph-container">
<app graph-nodes-url="${applicationBasePath}/metagrapher/graph.json"
     graph-style-url="${applicationBasePath}/css/metagrapher-graph.css"
     graph-config-url="${applicationBasePath}/metagrapher/config"
     class="graph-container"
>
    Loading...
</app>
<script src="js/application.js"></script>
</body>
</html>