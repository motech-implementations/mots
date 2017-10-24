const path = require('path');
const ROOT = path.resolve(__dirname, 'src/webapp');
const SRC = path.resolve(ROOT, 'js');
const DEST = path.resolve(__dirname, 'build/resources/main/static/site');

module.exports = {
    entry: {
        app: SRC + '/app.js'
    },
    module: {
        loaders: [
            {
                loaders: ['babel-loader?presets[]=es2015&presets[]=react'],
            }
        ]
    },
    output: {
        path: DEST,
        filename: 'bundle.js',
        publicPath: '/site/'
    }
};