import pytest
import sys
from flask import Flask

@pytest.fixture
def image():
    return "fry.jpg"

@pytest.fixture
def request_petition():
    return Flask.test_client
