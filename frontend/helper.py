import requests
from flask import Response

def forward_request(forward_base_url, request):
    """forward the request to determined url and return a Flask.Response"""
    while forward_base_url[-1] == '/':
        forward_base_url = forward_base_url[:-1]
    url = forward_base_url+request.path
    data = request.data
    if len(data) == 0:
        data = None

    print("Forwarding request to " + url)

    resp = requests.request(method=request.method,
                            url=url,
                            headers=dict(request.headers),
                            data=data)

    if 'Transfer-Encoding' in resp.headers:
        del resp.headers['Transfer-Encoding']

    return Response(headers=dict(resp.headers),
                    response=resp.text,
                    status=resp.status_code)
