// This file is required by the index.html file and will
// be executed in the renderer process for that window.
// All of the Node.js APIs are available in this process.

var data = [
    {
        label: 'node1', id: 1,
        children: [
            { label: 'child1', id: 2 },
            { label: 'child2', id: 3 }
        ]
    },
    {
        label: 'node2', id: 4,
        children: [
            { label: 'child3', id: 5 }
        ]
    }
];

$(function() {
    $('#tree1').tree({
        data: data
    });
});