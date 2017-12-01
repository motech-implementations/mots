const webpack = require("webpack");
const path = require('path');
const ROOT = path.resolve(__dirname, 'src/webapp');
const SRC = path.resolve(ROOT, 'js');
const DEST = path.resolve(__dirname, 'build/resources/main/static/site');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
  devtool: 'cheap-module-source-map',
  entry: {
      app: SRC + '/index.js'
  },
  output: {
    path: DEST,
    filename: 'bundle.js',
    publicPath: '/site/'
  },
  stats: {
    colors: true
  },
  plugins: [
    new webpack.ProvidePlugin({
      $: "jquery",
      jQuery: "jquery"
    }),
    new ExtractTextPlugin('bundle.css', {
      allChunks: true,
    }),
  ],
  module: {
    loaders: [
      {
        enforce: 'pre',
        test: /\.js$/,
        exclude: /node_modules/,
        loaders: ['eslint-loader']
      },
      {
        test: /\.js$/,
        loaders: ['babel-loader?presets[]=es2015&presets[]=react&presets[]=stage-1'],
        include: SRC,
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        use: ExtractTextPlugin.extract({ fallback: 'style-loader', use: ['css-loader'] })
      },
      {
        test: /\.scss$/,
        use: ExtractTextPlugin.extract('css-loader!sass-loader'),
      },
      { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, use: "file-loader" },
      { test: /\.(woff|woff2)$/, use: "url-loader?prefix=font/&limit=5000" },
      { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, use: "url-loader?limit=10000&mimetype=application/octet-stream" },
      { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, use: "url-loader?limit=10000&mimetype=image/svg+xml" }
    ]
  }
};