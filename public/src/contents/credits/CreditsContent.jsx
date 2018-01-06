import React from "react";
import { connect } from "react-redux";
import style from "./credits.css";

class CreditsContent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className={style.root}>
        <h2>CREDITS</h2>
        <div>
          <div>
            <h4>PROJECT CARS 2</h4>
            <p>{"https://www.projectcarsgame.com/"}</p>
          </div>
          <div>
            <h4>Project CARS Official Forum</h4>
            <p>{"http://forum.projectcarsgame.com/forum.php"}</p>
          </div>
          <div>
            <h4>CrewChief</h4>
            <p>{"http://thecrewchief.org"}</p>
          </div>
          <div>
            <h4>scala</h4>
            <p>{"Copyright (c) 2002-2017 EPFL"}</p>
            <p>{"Copyright (c) 2011-2017 Lightbend, Inc."}</p>
          </div>
          <div>
            <h4>akka</h4>
            <p>{"Copyright (C) 2009-2017 Lightbend Inc. http://www.lightbend.com"}</p>
          </div>
          <div>
            <h4>logback</h4>
            <p>{"Copyright (C) 1999-2015, QOS.ch. All rights reserved."}</p>
          </div>
          <div>
            <h4>scala-logging</h4>
            <p>{"Copyright 2014 Typesafe Inc. http://www.typesafe.com"}</p>
          </div>
          <div>
            <h4>react</h4>
            <p>{"Copyright (c) 2013-present, Facebook, Inc."}</p>
          </div>
          <div>
            <h4>redux</h4>
            <p>{"Copyright (c) 2015-present Dan Abramov"}</p>
          </div>
          <div>
            <h4>MapDB</h4>
            <p>{"Apache LicenseVersion 2.0, January 2004"}</p>
          </div>
        </div>
      </div>
    );
  }
}

export default CreditsContent;
