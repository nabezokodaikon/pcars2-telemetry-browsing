import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";

class CreditsContent extends React.Component {
  constructor(props) {
    super(props);
  }

  getFlexContainerStyle() {
    return {
      display: "flex",
      flexDirection: "column",
      alignItems: "left",
      width: "100%",
      marginLeft: "4rem"
    };
  }

  getCreditsStyle() {
    return {
      display: "flex",
      flexDirection: "row",
      flexWrap: "wrap",
      alignItems: "strech",
      alignContent: "strech"
    };
  }

  getCreditStyle() {
    return {
      width: "30%",
      height: "6rem",
      textAlign: "left",
      marginBottom: "2rem"
    }
  }

  createCredits() {
    return (
      <div style={this.getCreditsStyle()}>
        <div style={this.getCreditStyle()}>
          <h3>PROJECT CARS 2</h3>
          <p>{"https://www.projectcarsgame.com/"}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>Project CARS Official Forum</h3>
          <p>{"http://forum.projectcarsgame.com/forum.php"}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>CrewChief</h3>
          <p>{"http://thecrewchief.org"}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>scala</h3>
          <p>{"Copyright (c) 2002-2017 EPFL"}</p>
          <p>{"Copyright (c) 2011-2017 Lightbend, Inc."}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>akka</h3>
          <p>{"Copyright (C) 2009-2017 Lightbend Inc. http://www.lightbend.com"}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>logback</h3>
          <p>{"Copyright (C) 1999-2015, QOS.ch. All rights reserved."}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>scala-logging</h3>
          <p>{"Copyright 2014 Typesafe Inc. http://www.typesafe.com"}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>react</h3>
          <p>{"Copyright (c) 2013-present, Facebook, Inc."}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>redux</h3>
          <p>{"Copyright (c) 2015-present Dan Abramov"}</p>
        </div>
        <div style={this.getCreditStyle()}>
          <h3>MapDB</h3>
          <p>{"Apache LicenseVersion 2.0, January 2004"}</p>
        </div>
      </div>
    );
  }

  render() {
    return (
      <div style={this.getFlexContainerStyle()}>
        <h2>CREDITS</h2>
        {this.createCredits()}
      </div>
    );
  }
}

export default CreditsContent;
