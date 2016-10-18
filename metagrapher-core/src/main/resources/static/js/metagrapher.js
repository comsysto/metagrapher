(function () {


    var node = null;
    var config;
    $(function () {
        config = JSON.parse($('script#config').text());

        $.getJSON(config.graphUrl).then(function (data) {
            createGraph(data.elements);
            rearrangeNodes();
            loadPositions();
            updateControlBox();
        });


        $('#store-layout-link').click(function () {
            $.ajax({
                url: config.configUrl,
                method: 'POST',
                dataType: 'json',
                data: JSON.stringify(cy.nodes().toArray().reduce(function (map, node) {
                    map[node.id()] = {position: node.position()};
                    return map;
                }, {})),
                contentType: 'application/json'
            })
        })

        $('#rearrange-layout-link').click(function () {
            rearrangeNodes();
        });

        $('#load-layout-link').click(function () {
            loadPositions();
        });
    });

    function rearrangeNodes(){
        cy.makeLayout({name: 'grid'}).run();
    }

    function loadPositions() {
        $.getJSON(config.configUrl).then(function (data) {
            cy.nodes().forEach(function (n) {
                var nodeData = data[n.id()];
                if (nodeData) {
                    n.position(nodeData.position);
                }
            });
        });
    }

    function updateControlBox(){
        updateInstances();
        updateProperties();
        updateAppLinks();
    }


    function updateAppLinks() {
        var element = $('#app-links');
        var panelElement = $('#app-links-panel');
        if (node && node.is('.service')) {
            var appLinks = node.data('appLinks');
            setupLink(".jenkins-link", appLinks.jenkinsLink);
            setupLink(".stash-link", appLinks.stashLink);
            setupLink(".homepage-link", appLinks.homePageLink);

            panelElement.show();
        } else {
            panelElement.hide();
        }

        function setupLink(selector, link){
            var linkElement = element.find(selector);
            if(link){
                linkElement.attr('href', link);
                linkElement.parent().removeAttr('style');
            } else {
                linkElement.parent().hide();
            }

        }

    }


    function updateInstances() {
        var instancesElement = $('#instances');
        var instancesPanelElement = $('#instances-panel');

        instancesElement.children().not('.template').remove();

        if (node && node.is('.service')) {
            var instances = node.data('instances');
            instances.forEach(function (instance) {

                var template = instancesElement.find('.template').clone();
                template.removeClass('template');
                template.find('.instance-label')
                    .text(instance.hostName + ':' + instance.port)
                    .addClass(instance.state.toLowerCase());

                template.find('.homepage-link')
                    .attr('href', instance.homePage);

                // don't use show ... it mess up with bootstrap ... and put style=list-item
                template.removeAttr('style');

                instancesElement.append(template);

            });
            instancesPanelElement.show();
        } else {
            instancesPanelElement.hide();
        }
    }

    function updateProperties() {
        var element = $('#properties');
        var panelElement = $('#properties-panel');

        element.children().remove();

        if (node && node.is('.service')) {
            var properties = node.data('properties');
            var dlElement = $('<dl>').addClass('dl-horizontal');
            Object.keys(properties).forEach(function (name) {
                var value = properties[name];
                dlElement.append($('<dt>').text(name + ':'));
                dlElement.append($('<dd>').text(value));

            });
            element.append(dlElement);
            panelElement.show();
        } else {
            panelElement.hide();
        }
    }


    function createGraph(graphElements) {
        var cy = window.cy = cytoscape({
                container: document.getElementById('cy'),

                ready: function () {
                },

                style: $('#graph-styles').text(),

                layout: {
                    name: 'null',
                },

                elements: graphElements
            }
        );

        cy.nodes('.service').on('position', function (e) {
            var serviceElement = e.cyTarget;
            var p = serviceElement.position();
            var h = serviceElement.boundingBox().h;
            var stateElements = serviceElement.neighborhood('node.state').sort(function (e1, e2) {
                return e1.data('order') - e2.data('order');
            });

            stateElements.forEach(function (element, i) {
                element.position({
                    x: p.x + (16 * i) - (stateElements.length / 2 * 16) + 8,
                    y: p.y + h / 4
                })
            })
        });

        cy.nodes().on('select', function (e) {
            node = e.cyTarget;
            updateControlBox();
        });

        cy.nodes().on('unselect', function (e) {
            node = null;
            updateControlBox();
        });

    }

}());
