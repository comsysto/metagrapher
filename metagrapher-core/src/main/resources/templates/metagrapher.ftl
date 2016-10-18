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
    <script id="config" type="application/json">
        {
            "graphUrl":  "${applicationBasePath}/metagrapher/graph.json",
            "configUrl": "${applicationBasePath}/metagrapher/config"
        }
    </script>
    <style id="graph-styles" rel="graph-styles">
        node {
            content: data(name);
            border-color: grey;
        }

        node.state {
            shape: ellipse;
            border-width: 1px;
            width: 15px;
            height: 15px;
            content: data(count);
            text-valign: center;
            font-size: 10px;
            font-weight: bold;
            color: whitesmoke;
        }

        node.down {
            background-color: lightskyblue;
        }

        node.starting {
            background-color: darkgoldenrod;
        }

        node.unknown {
            background-color: grey;
        }

        node.out_of_service {
            background-color: red;
        }

        node.up {
            background-color: greenyellow;
        }

        node.state.down {
            background-color: darkblue;
        }

        node.state.starting {
            background-color: darkgoldenrod;
        }

        node.state.unknown {
            background-color: darkgray;
        }

        node.state.out_of_service {
            background-color: darkred;
        }

        node.state.up {
            background-color: darkgreen;
        }


        edge.import-export-edge {
            target-arrow-shape: triangle;
            content: data(name);
            line-style: dashed;
            curve-style: unbundled-bezier;
            width: 3;
        }

        edge.service-export-edge {
            width: 5;
        }

        node.external-system {
            shape: triangle;
            border-width: 3px;
            background-color: lightgray;
            font-weight: bold;
            /*color:darkgray;*/
            text-valign: center;
            text-outline-color: lightgrey;
            text-outline-opacity: 1;
            text-outline-width: 1px;
            width: 50px;
            height: 50px;
        }

        node.service {
            shape: roundrectangle;
            border-width: 3px;
            font-weight: bold;
            /*color:darkgray;*/
            text-valign: center;
            text-outline-color: lightgrey;
            text-outline-opacity: 1;
            text-outline-width: 1px;
            width: 100px;
            height: 100px;
        }

        node:selected {
            overlay-color: lightblue;
            overlay-opacity: 0.5;
        }

        node.export {
            shape: ellipse;
            color: darkgray;
            border-width: 3px;
            background-color: lightgray;
            border-width: 3px;
            font-size: 10px;
            width: 30px;
            height: 30px;
        }
    </style>
    <style>
        #control-box {
            float: left;
            position: absolute;
            top: 10px;
            left: 10px;
            z-index: 1;
            padding: 15px;
            min-width: 400px;
            border: solid lightgray;
            border-radius: 10px;

            background: rgba(233, 233, 233, 0.7);
        }

        .out_of_service {
            color: darkred;
        }

        .up {
            color: darkgreen;
        }

        .down {
            color: darkblue;
        }

        .starting {
            color: darkgoldenrod;
        }

        .unknown {
            color: darkgray;
        }

        .tip > p {
            font-size: 12px;
        }
    </style>
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

<script src="${applicationBasePath}/libs/jquery.js"></script>
<script src="${applicationBasePath}/libs/bootstrap-3.3.4-dist/js/bootstrap.js"></script>
<script src="${applicationBasePath}/libs/jquery.qtip.js"></script>
<script src="${applicationBasePath}/libs/cytoscape.js"></script>
<script src="${applicationBasePath}/libs/cytoscape-qtip.js"></script>
<script src="${applicationBasePath}/js/metagrapher.js"></script>
</body>


</html>
