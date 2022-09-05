cd "load_generator" || exit
python -m venv venv
venv/bin/pip install -r requirements.txt
venv/bin/python -m run

#Clean up
rm -rf "venv"
cd ..