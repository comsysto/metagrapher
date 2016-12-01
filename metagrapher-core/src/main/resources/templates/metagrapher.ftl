<!DOCTYPE>

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
<div id="control-box">
    <h1>Metagrapher</h1>
    <hr/>
    <div class="panel-group">
        <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading" data-toggle="collapse" data-target="#layout" style="cursor: pointer">
                Layout
            </div>
            <div class="panel-body collapse" id="layout">
                <a class="btn btn-l btn-primary" id="store-layout-link">store</a>
                <a class="btn btn-l btn-primary" id="load-layout-link">load</a>
                <a class="btn btn-l btn-warning" id="rearrange-layout-link">rearrange</a>
            </div>
        </div>
        <div id="app-links-panel" class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading" data-toggle="collapse" data-target="#app-links" style="cursor: pointer">
                Application Links
            </div>
            <ul class="list-group collapse" id="app-links">
                <li class="template list-group-item">
                    <a class="jenkins-link " target="_blank">
                        <span class="glyphicon glyphicon-wrench"></span>
                        Jenkins Job
                    </a>

                </li>
                <li class="template list-group-item">
                    <a class="stash-link" target="_blank">
                        <span class="glyphicon glyphicon-equalizer"></span>
                        Stash Repo
                    </a>

                </li>
                <li class="template list-group-item">
                    <a class="homepage-link" target="_blank">
                        <span class="glyphicon glyphicon-home"></span>
                        Home Page
                    </a>
                </li>
            </ul>
        </div>
        <div id="instances-panel" class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading" data-toggle="collapse" data-target="#instances" style="cursor: pointer">
                Instances
            </div>
            <ul class="list-group collapse" id="instances">
                <li class="template list-group-item" style="display: none;">
                    <span class="instance-label"></span>
                    <a class="homepage-link ">
                        <span class="glyphicon glyphicon-home"></span>
                    </a>
                </li>
            </ul>
        </div>
        <div id="properties-panel" class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading" data-toggle="collapse" data-target="#properties" style="cursor: pointer">
                Properties
            </div>
            <div class="panel-body collapse" id="properties">
            </div>
        </div>
    </div>

</div>

<div id="cy" style="width: 100%; height: 100%;"></div>

<script src="system.js"></script>
<script>
    // set our baseURL reference path
    SystemJS.config({
        baseURL: '/js'
    });

    // loads /js/main.js
    SystemJS.import('main.js');
</script>

<script src="${applicationBasePath}/libs/jquery.js"></script>
<script src="${applicationBasePath}/libs/bootstrap-3.3.4-dist/js/bootstrap.js"></script>
<script src="${applicationBasePath}/libs/jquery.qtip.js"></script>
<script src="${applicationBasePath}/libs/cytoscape.js"></script>
<script src="${applicationBasePath}/libs/cytoscape-qtip.js"></script>
<script src="${applicationBasePath}/js/metagrapher.js"></script>
</body>


</html>
