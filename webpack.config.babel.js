import path from "path";
import CleanWebpackPlugin from "clean-webpack-plugin";
// import webpack from "webpack";

module.exports = {
  entry: {
    index: "./public/src/example.js",
    main: "./public/src/main.js",
    react: "./public/src/react.jsx",
    car_state_data: "./public/src/car_state_data.js"
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
        use: { loader: "babel-loader" }
      },
      {
        test: /\.css$/,
        use: [
          { loader: "style-loader" },
          { loader: "css-loader" }
        ]
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
