#!/bin/bash

pip install pre-commit

mvn clean package -DskipTests

pre-commit install
