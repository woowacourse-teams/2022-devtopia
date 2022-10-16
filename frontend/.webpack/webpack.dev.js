const webpack = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'eval-source-map',
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        use: 'ts-loader',
      },
    ],
  },
  plugins: [
    new webpack.EnvironmentPlugin({
      SERVICE_URI: 'https://test.levellog.app',
      API_URI: 'https://dev.levellog.app/api',
      CLIENT_ID: '00230a7d7fa77d726d7e',
    }),
    new ForkTsCheckerWebpackPlugin(),
  ],
});
