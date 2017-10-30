import path from "path";
import CleanWebpackPlugin from "clean-webpack-plugin";
import webpack from "webpack";

module.exports = {
  entry: {
    index: ["babel-polyfill", "./public/src/index.jsx"],
    debug: ["babel-polyfill", "./public/src/debug.jsx"]
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
          {
            loader: "style-loader",
            options: {
              modules: true
            }
          },
          { 
            loader: "css-loader",
            options: {
              modules: true
            }
          }
        ]
      },
      {
        test: /\.(png|jpg|gif)$/,
        loader: "file-loader"
      }
    ]
  },
  plugins: [
    new CleanWebpackPlugin(["public/dist"])
  ]
}
