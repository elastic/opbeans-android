cd "loader" || exit
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python -m run