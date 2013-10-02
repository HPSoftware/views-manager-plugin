
var iconsUrl = iconsRootUrl;

var nestedViewIconLocation = iconsRootUrl + "nestedViewIcon.png";
var listViewIconLocation = iconsRootUrl + "listViewIcon.png";
var jobIconLocation = iconsRootUrl + "jobIcon.png";
var unknownViewIconLocation = iconsRootUrl + "unknownViewIcon.png";
var allViewIconLocation = iconsRootUrl + "allViewIcon.png";
var jenkinsIconLocation = iconsRootUrl + "jenkinsIcon.png";

/*
    var sampleModel =  [
        {
            //name to display
            "data" : "Jenkins",

            //attributes
            "attr" : {

                //type attribute
                "rel" : "Jenkins"
            },

            //child nodes
            "children" : [

                {   "data" : "job",
                    "attr" : {
                        "rel": "Job"
                    }

                },
                {   "data" : "list",
                    "attr" : {
                        "rel": "ListView"
                    }

                },
                {   "data" : "nested",
                    "attr" : {
                        "rel": "NestedView"
                    }
                }
            ]
        }
    ];
    //*/
var treeModel = null;
var newNodeCount = 10;


$(function () {

    //function that initializes the model
    spinner_on();
    jsProxy.tree(
        function (rsp) {

            spinner_off();

            /*
            ret = rsp.responseObject();
            eval("treeModel = " + rsp.responseText + ";");
            //*/
            treeModel = rsp.responseObject();
            //treeModel = sampleModel;

            initTree();
        }
    );
});

function initTree () {

    $("#viewsTree")


        //alerts each operation before it happens...
//       .bind("before.jstree", function (e, data) {
//           alert("func:" + data.func + "<br />" +
//                "event:" + e + "<br />" +
//                "data:" + data + "<br />");
//       })

        //bind event to when tree is fully loaded
        .bind("loaded.jstree", function (arg, data) {
            //try and restore tree state if possible
            data.inst.reopen();

            //if root node is not opened,
            if (!data.inst.is_open(getRoot())) {
                //open everything
                data.inst.open_all(-1);
            }
        })

       //start configuring the javascript tree
       .jstree({

           // List of active plugins
           "plugins" : [

                //support for themes
               "themes",
               //initialization from json
               "json_data",
               //different types of nodes
               "types",
               //drag and drop
               "dnd",
               //right click menu
               "contextmenu",
               //highlighting and marking nodes of the tree
               "ui",  //nathang: (this apparently has bugs, selects the same node multiple times, causes failures
               // for example, rename a view and then try and move/delete it. sometimes fails.)
               //binding tree edit events
               "crrm",
               //support for remembering opened nodes in cookie
               "cookies"
               //"hotkeys"
           ],

           "ui" : {
                "select_limit" : 1
           },

           //data to display in the tree
           "json_data" :
                //function () { ret jsProxy.tree() },
                treeModel,

           // Using types - most of the time this is an overkill
           // read the docs carefully to decide whether you need types
           //*

           //TODO: this information should be retrieved from the server
           "types" : {
               // I set both options to -2, as I do not need depth and children count checking
               // Those two checks may slow jstree a lot, so use only when needed
               "max_depth" : -2,
               "max_children" : -2,

    //           only support NestedView as a root node
    //           (we wrap the Jenkins view collection with a nested view, thus avoiding working on a forest,
    //           instead working on a single "unifying" tree whose immediate children are the real trees)
               "valid_children" : [ "Jenkins" ],

               "types" : {

                    // the jenkins root node
                   "Jenkins" : {
                       "valid_children" : "all",

                       // If we specify an icon for the default type it WILL OVERRIDE the theme icons
                       "icon" : {
                           "image" : jenkinsIconLocation
                       },

                        //disable certain operations - copying deleting renaming etc..
                       "start_drag" : false,
                       "move_node" : false,
                       "delete_node" : false,
                       "remove" : false,

                       //not implemented yet
                       "create": false,
                       "rename": false
                   },

                   // an actual Job, not a view, in our case - it's a leaf.
                   "Job" : {
                       // I want this type to have no children (so only leaf nodes)
                       // In my case - those are files
                       "valid_children" : "none",

                       // If we specify an icon for the default type it WILL OVERRIDE the theme icons
                       "icon" : {
                           "image" : nestedViewIconLocation
                       }
                   },

                   "AllView" : {
                       // I want this type to have no children (so only leaf nodes)
                       // In my case - those are files
                       "valid_children" : "none",

                       // If we specify an icon for the default type it WILL OVERRIDE the theme icons
                       "icon" : {
                           "image" : allViewIconLocation
                       },

                        /*
                        //disable certain operations - copying deleting renaming etc..
                       "start_drag" : false,
                       "move_node" : false,
                       "delete_node" : false,
                       "remove" : false,

                       //not implemented yet
                       "rename": false
                       */
                       "create": false,

                   },

                   "UnknownViewType" : {
                       // I want this type to have no children (so only leaf nodes)
                       // In my case - those are files
                       "valid_children" : "none",

                       // If we specify an icon for the default type it WILL OVERRIDE the theme icons
                       "icon" : {
                           "image" : unknownViewIconLocation
                       },

                        /*
                       //disable certain operations - copying deleting renaming etc..
                       "start_drag" : false,
                       "move_node" : false,
                       "delete_node" : false,
                       "remove" : false,
                       "rename" : false,

                       //not implemented yet
                       "create": false
                       */
                   },

                   // The `ListView` type, contains a group of Jobs
                   "ListView" : {

                       // can have files and other folders inside of it, but NOT `drive` nodes
                       "valid_children" : [ "Job" ],

                       "icon" : {
                           "image" : listViewIconLocation
                       },

                       //not implemented yet
                       "create": false
                   },

                   // The `NestedView` type, contains other views
                   "NestedView" : {

                       "valid_children" : "all",

                       "icon" : {
                           "image" : nestedViewIconLocation
                       },

                       //not implemented yet
                       "create": false
                   }

               }
           },

    //       //the UI plugin - it handles selecting/deselecting/hovering nodes
    //       "ui" : {
    //           // this makes the node with ID node_4 selected onload
    //           "initially_select" : [ ]
    //       },
    //
    //       // the core plugin - not many options here
    //       "core" : {
    //           // just open those two nodes up
    //           // as this is an AJAX enabled tree, both will be downloaded from the server
    //           "initially_open" : [ ]
    //       }

       })

        .bind("rename.jstree", function (e, data) {

            /*
            var nodeId = getNodeId(data);
            var parentId = getParentNodeId(data);
            jsProxy.add(parentId, nodeId, proxyHandler(data));
            */

            var dataIdentifier = getIdentifier(data, data.inst);
            var newName = data.rslt.new_name;

            //don't display any changes until the proxyHandler below gets a response with confirmed changes from server
            revert(data);
            spinner_on();

            //TODO spinner_on() here; and spinner_off in proxyHandler(data)
            try {
                jsProxy.rename(dataIdentifier, newName, proxyHandler(data));
            }
            catch (e) {
                recover(e, data);
            }
       })

       .bind("create.jstree", function (e, data) {

            /*
            var nodeId = getNodeId(data);
            var parentId = getParentNodeId(data);
            jsProxy.add(parentId, nodeId, proxyHandler(data));
            */
            alert("create operation not implemented yet");
            refresh(data);
       })

       .bind("delete_node.jstree", function (e, data) {

            var dataIdentifier = getIdentifier(data, data.inst);

            revert(data);

            if (confirm("are you sure you want to delete the selected view and all of its subviews?")) {

                spinner_on();
                try {
                    jsProxy.delete(dataIdentifier, proxyHandler(data));
                }
                catch (e) {
                    recover(e, data);
                }
            }
       })

       .bind("move_node.jstree", function (e, data) {

            var nodeData = data.rslt.o;
            var nodeId = getIdentifier(nodeData, data.inst);

            var newParentData = data.rslt.np;
            var parentId = getIdentifier(newParentData, data.inst);

            revert(data);
            spinner_on();

            try {
                if (data.rslt.cy) {
                    jsProxy.clone(nodeId, parentId, proxyHandler(data));
                }
                else {
                    jsProxy.move(nodeId, parentId, proxyHandler(data));
                }
            }
            catch (e) {
                recover(e, data);
            }

       });
}

function spinner_on() {
    $("#ajax_loading_indicator").show();
}

function spinner_off() {
    $("#ajax_loading_indicator").hide();
}

function revert(data) {

    var inst = data.inst;
    var settings = inst._get_settings();
    settings.json_data.data = treeModel;
    inst._set_settings(settings);

    inst.refresh(-1);
}

function refresh(data) {
    jsProxy.tree(
        function (rsp) {

            var inst = data.inst;
            var settings = inst._get_settings();
            treeModel = rsp.responseObject().data;
            settings.json_data.data = treeModel;
            inst._set_settings(settings);

            inst.refresh(-1);
            spinner_off();
        }
    );
}

function getNodeObject(data, inst) {

    var res;

    if (data.rslt) {
         res = (data.rslt.o) ?
            data.rslt.o :
            data.rslt.obj;
    }
    else {
        res = data;
    }

    //there's a bug where we sometimes get multiple instances of the same object... but in UI plugin settings we
    //only allow one instance to be selected, so if we see an array - we know it's the bug and they're all
    //duplicates, and it's safe to ignore and just choose the first element...
     if (res.length && (res.length > 1)) {
        res = res[res.length - 1];
        res = inst._get_node(res);
     }

    return res;
}

function getNodeId(data, inst) {

    var obj = getNodeObject(data, inst);

    return obj.data("node_id");
}

function getTreeId(data, inst) {

    var obj = getNodeObject(data, inst);

    return obj.data("tree_id");
}

function getParentNode(data) {

    return (data.rslt.r) ?
        data.rslt.r :
        data.rslt.parent;
}

function getRoot() {
    return $("#viewsTree").find("li:first");
}

function proxyHandler(data) {

    return function(rsp) {

        var obj = rsp.responseObject();

        if (obj.message) {
            alert("operation failed: " + obj.message);
        }

        refresh(data);
    };
}

function recover(e, data) {
    alert("operation failed: " + e);
    refresh(data);
}

function getIdentifier(data, inst) {

    var treeId = getTreeId(data, inst);
    var nodeId = getNodeId(data, inst);
    var identifier = new Array(new Array(treeId, nodeId));

    //identifier = { "keychain" : keychain };

    //*
    identifier = JSON.stringify(identifier);
    identifier = identifier.substring(1,identifier.length-1);
    //*/

    return identifier;
}