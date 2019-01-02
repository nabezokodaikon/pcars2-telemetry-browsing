import path from "path";
import CleanWebpackPlugin from "clean-webpack-plugin";
import webpack from "webpack";

module.exports = {
  entry: {
    index: ["babel-polyfill", "whatwg-fetch", "./public/src/indexProduct.jsx"]
  },
  output: {
    filename: "index.bundle.js",
    path: path.resolve(__dirname, "public/dist")
  },
  optimization: {
    minimize: true
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
            loader: "style-loader"
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
    new CleanWebpackPlugin(["public/dist"]),
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: JSON.stringify('production')
      }
    })
  ]
}
