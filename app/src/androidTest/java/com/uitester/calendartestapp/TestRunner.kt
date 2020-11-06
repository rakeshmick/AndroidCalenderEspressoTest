package com.uitester.calendartestapp.test

import io.cucumber.junit.CucumberOptions


@CucumberOptions(
    glue = ["com.uitester.calendartestapp.test"],
    features = ["features"]
)
class MyTests