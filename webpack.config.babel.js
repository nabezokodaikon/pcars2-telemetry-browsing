import path from "path";
import CleanWebpackPlugin from "clean-webpack-plugin";

module.exports = {
  entry: {
    index: "./public/src/example.js",
    main: "./public/src/main.js",
    react: "./public/src/react.js"
  },
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
    new CleanWebpackPlugin(["public/dist"])
  ]
}
