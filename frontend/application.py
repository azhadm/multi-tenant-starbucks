import uuid
import requests
from flask import Flask, Response, redirect, url_for, render_template, json, session, request
from helper import forward_request

application = Flask(__name__)

application.config["SERVER_HOST"] = "0.0.0.0"
application.config["SERVER_PORT"] = 8080

application.config["KONG_BASE_ADDR"] = 'http://localhost:9090'
# application.config["KONG_BASE_ADDR"] = 'http://restbucks-api-sanjose-lb-1067926920.us-west-1.elb.amazonaws.com:9090'
@application.route('/ping', methods=['GET'])
def ping():
    """for pinging purpose"""
    return Response(status=200)

@application.route('/starbucks/order/<string:order_id>', methods=['GET'])
def get_order(order_id):
    """forward the get all orders request to kong API
    and forward the response to client"""
    return forward_request(forward_base_url=application.config['KONG_BASE_ADDR'],
                           request=request)

@application.route('/starbucks/order/<string:order_id>', methods=['DELETE'])
def cancel_order(order_id):
    """forward the get all orders request to kong API
    and forward the response to client"""
    return forward_request(forward_base_url=application.config['KONG_BASE_ADDR'],
                           request=request)

@application.route('/starbucks/orders', methods=['GET'])
def get_all_orders():
    """forward the get all orders request to kong API
    and forward the response to client"""
    resp = forward_request(forward_base_url=application.config['KONG_BASE_ADDR'],
                           request=request)
    del resp.headers['Transfer-Encoding']
    return resp

@application.route('/starbucks/order/<string:order_id>', methods=['PUT'])
def update_order(order_id):
    """forward the place order request to kong API
    and forward the response to client"""
    return forward_request(forward_base_url=application.config['KONG_BASE_ADDR'],
                           request=request)

@application.route('/starbucks/order', methods=['POST'])
def place_order():
    """forward the place order request to kong API
    and forward the response to client"""
    return forward_request(forward_base_url=application.config['KONG_BASE_ADDR'],
                           request=request)

@application.route('/starbucks/order/<string:order_id>/pay', methods=['POST'])
def pay_order(order_id):
    """forward the place order request to kong API
    and forward the response to client"""
    return forward_request(forward_base_url=application.config['KONG_BASE_ADDR'],
                           request=request)


@application.route('/', methods=['GET'])
def index():
    """render index template (homepage)"""
    return 'Hello World!'
    # if 'id' not in session:
    #     session['id'] = uuid.uuid4().hex
    # return render_template("index.html")

if __name__ == "__main__":
    application.run(debug=True,
                    host=application.config["SERVER_HOST"],
                    port=application.config["SERVER_PORT"])