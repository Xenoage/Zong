echo "Downloading Zong! libraries..."
rm lib/*.jar lib/*.txt
for line in `cat lib/lib.list`; do
 wget --directory-prefix=lib $line
done
echo "Downloading Zong! libraries finished."