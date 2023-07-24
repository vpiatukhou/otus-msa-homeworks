The steps to run the application with Docker:

1. Clone the repository.
2. Run the following commands from the root directory.

Build an image:

`docker build -t otus-msa-hw-01 .`

Run a container:

`docker run --name otus-msa-hw-01 -p 8000:8000 otus-msa-hw-01`

Test the application:

`curl http://localhost:8000/health`

The expected response:

`{"status":"OK"}`