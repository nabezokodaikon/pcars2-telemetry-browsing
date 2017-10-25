import path from "path";
import CleanWebpackPlugin from "clean-webpack-plugin";
import webpack from "webpack";

module.exports = {
  entry: {
    index: "./public/src/index.jsx",
    debug: "./public/src/debug.jsx"
  },
  devtool: 'inline-source-map',
  output: {
    filename: "[name].bundle.js",
    path: path.resolve(__dirname, "public/dist")
  },
  module: {
    rules: [
      {
        test: /(\.js$|\.jsx$)/,
        exclude: /(node_modules|bower_components)/,
        loader: "babel-loader"
      },
      {
        test: /\.css$/,
        use: [
          { loader: "style-loader" },
          { loader: "css-loader" }
        ]
      },
      {
        test: /\.(png|jpg|gif)$/,
        loader: "file-loader"
      }
    ]
  },
  plugins: [
    new CleanWebpackPlugin(["public/dist"]) /*,
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: JSON.stringify('production')
      }
    }),
    new webpack.optimize.UglifyJsPlugin()
    */
  ]
}
